//
// Created by Ander on 26/01/2022.
//

#ifndef OVING2_WORKERS_H
#define OVING2_WORKERS_H


#include <functional>

class Workers {

public:
    Workers(int i);

    static void start();

    void stop();

    void post(std::function<void()> task);

    void join(std::string t);

    void join(std::string t, int numOfThreads);
};


#endif //OVING2_WORKERS_H
