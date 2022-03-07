const net = require('net');
const crypto = require('crypto')

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    
    <div>
    <input type="text" id="inputField">
    <button id="submitButton">Submit</button>
    <div id="chatBox"></div>
    </div>
    
    <script>
      let ws = new WebSocket('ws://localhost:3001');
      
      let field = document.getElementById("inputField")
      let button = document.getElementById("submitButton")
      let chatBox = document.getElementById("chatBox")
      
      button.addEventListener("click", () => {
          chatBox.innerHTML += "You: "+field.value + "</br>"
          ws.send(field.value)
      } )
      
      ws.onmessage = event => chatBox.innerHTML += event.data + "</br>";
      
    </script>
  </body>
</html>
`;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});

let connections = [];
const wsServer = net.createServer((connection) => {

    connection.on('data', (data) => {
        let key;
        if((key = getKey(data))!= null){
            let acceptString = getAcceptString(key)
            connection.write("HTTP/1.1 101 Switching Protocols\r\n");
            connection.write("Upgrade: websocket\r\n");
            connection.write("Connection: Upgrade\r\n");
            connection.write("Sec-WebSocket-Accept: " + acceptString + "\r\n\r\n");
            console.log("Connected")
            connections.push(connection)
        } else {

            const decoded = decodeMessage(data)
            console.log(decoded)
            let message = "Another user: "+decoded

            let response = createBufferFromMessage(message)
            sendToAll(response, connection)
        }
    });

    connection.on('end', () => {
        console.log('Client disconnected');
    });
});
wsServer.on('error', (error) => {
    console.error('Error: ', error);
});
wsServer.listen(3001, () => {
    console.log('WebSocket server listening on port 3001');
});

function getKey(data){
    const array = data.toString().split("\n")
    for (let i = 0; i < array.length; i++) {
        if (array[i].includes("Sec-WebSocket-Key")){
           return array[i].split(" ")[1].trim()
        }
    }
}

function getAcceptString(key){
    return base64(SHA1(key))
}

function SHA1(key){
    const rfc6455 = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    return crypto.createHash("sha1").update(key+ rfc6455, "binary")
}

function base64(hashedKey){
    return hashedKey.digest("base64")
}

function decodeMessage(data) {
    let bytes = Buffer.from(data)
    let length = bytes[1] & 127;
    let maskStart = 2;
    let dataStart = maskStart + 4;
    let msg = "";
    for (let i = dataStart; i < dataStart + length; i++) {
        let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart) % 4)];
        msg += String.fromCharCode(byte);
    }
    return msg;
}
function createBufferFromMessage(message) {
    let sub1 = Buffer.from([0x81, message.length]);
    let sub2 = Buffer.from(message, "utf-8");
    return Buffer.concat([sub1, sub2]);
}

function sendToAll(buffer, connection){
    for (let i = 0; i < connections.length; i++) {
        if(connections[i]!==connection) connections[i].write(buffer)
    }
}
