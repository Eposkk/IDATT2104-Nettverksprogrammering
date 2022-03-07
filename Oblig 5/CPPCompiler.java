package ntnu.adriawh.backend.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.*;


@RestController
//@RequestMapping("v1")
public class CPPCompiler {

    Logger logger = LoggerFactory.getLogger(CPPCompiler.class);

    @PostMapping  ("/compile")
    public String compile(@RequestBody String program) throws IOException {

        logger.info("Request from client received");

        File cppFile = new File("docker/cppFile.cpp");

        FileWriter writer = new FileWriter("docker/cppFile.cpp");

        logger.info(program);
        writer.write(program);
        writer.close();

        logger.info("building");
        var build = Runtime.getRuntime().exec("docker build ./docker -t cpp_image");
        logger.info(new String(build.getErrorStream().readAllBytes()));

        logger.info("running");
        var run = Runtime.getRuntime().exec("docker run --rm cpp_image");
        String buildError = new String(run.getErrorStream().readAllBytes());
        logger.info(buildError);


        BufferedReader input = new BufferedReader(new InputStreamReader(run.getInputStream()));

        String response ="";
        String s;
        while((s = input.readLine()) != null){
            response += s;
        }

        return response;

    }

}
