#include "Workers.h"
# include <functional>
# include <iostream>
# include <list>
# include <mutex>
# include <thread>
# include <vector>
# include <condition_variable>

using namespace std;

vector<thread> worker_threads;
list<function<void()>> tasks;
mutex tasks_mutex; // tasks mutex needed
condition_variable cv;
int numOfThreads;
bool running;
bool gettingTask;

Workers::Workers(int i) {
    numOfThreads = i;
    running = true;
    gettingTask= true;
}


void Workers::start() {
    for(int j=0; j<numOfThreads; j++){
        worker_threads.emplace_back([]  {

            while(running){
                function<void()> task;
                {
                    unique_lock<mutex> lock(tasks_mutex);
                    while (gettingTask) {
                        cv.wait(lock);
                    }
                    if (!tasks.empty()){
                        task = *tasks.begin();
                        tasks.pop_front();
                    }
                }
                if (task){
                    task();
                }
            }
        });

        {
            unique_lock<mutex> lock(tasks_mutex);
            gettingTask= false;
        }
        cv.notify_one();
    }
}

void Workers::post(function<void()> task) {
    tasks.emplace_back(task);
}

void Workers::join(string t, int numOfThreads){
    for (int i = 0; i < numOfThreads; ++i) {
        if (worker_threads.at(i).joinable()){
            worker_threads.at(i).join();
        }
        cout <<numOfThreads;
        cout<<t <<i<< endl;
    }
}

void Workers::stop(){
    while(running){
        if (tasks.empty()){
            running = false;
            gettingTask= false;
        }
    }
}
