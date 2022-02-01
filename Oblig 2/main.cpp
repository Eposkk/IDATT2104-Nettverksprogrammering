#include <iostream>
#include "Workers.h"

using namespace std;

void taskA(){
    cout << "Hei fra task A: Running in parallell" << endl;
}
void taskB(){
    cout << "Hei fra task B: Running in parallell" << endl;
}
void taskC(){
    cout << "Hei fra task C running as an eventloop" << endl;
}
void taskD(){
    cout << "Hei fra task D running as an eventloop" << endl;
}

int main() {
    Workers worker_threads(4);
    Workers event_loop(1);
    worker_threads.start(); // Create 4 internal worker_threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post(taskA);
    // Task A

    worker_threads.post(taskB);
    // Task B
    // Might run in parallel with task A


    event_loop.post(taskC);
    // Task C
    // Might run in parallel with task A and B

    event_loop.post(taskD);
    // Task D
    // Will run after task C
    // Might run in parallel with task A and B

    worker_threads.stop();
    event_loop.stop();

    worker_threads.join("worker",4); // Calls join() on the worker worker_threads
    event_loop.join("event",1); // Calls join() on the event thread

}


