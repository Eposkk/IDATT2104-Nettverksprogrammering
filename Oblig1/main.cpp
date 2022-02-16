#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <list>

using namespace std;

bool isPrime(int i){
    bool isPrime = true;

    if (i==1){
        return false;
    }
    for (int j = 2; j < i/2; ++j) {
        if (i%j==0){
            isPrime= false;
            break;
        }
    }
    return isPrime;
}

mutex mtx;

int main() {
    vector<thread> threads;
    list<int> primes;
    int numberOfThreads=5;
    int start = 1;
    int end = 1000;
    int blockSize = (end-start)/numberOfThreads;
    int currentIndex;

    for (int i=0; i < numberOfThreads; i++){
        if (currentIndex>end){
            break;
        }
        threads.emplace_back([i, &start, &end, &blockSize, &primes, &currentIndex]{
            for (int j = start+i*blockSize; j < start + (i+1)*blockSize; ++j) {
                currentIndex = j;
                if (currentIndex>end){
                    break;
                }
                if (isPrime(j)){
                    mtx.lock();
                    primes.push_back(j);
                    mtx.unlock();
                }
            }
        });
    }

    for (auto &thread : threads) {
        thread.join();
    }

    primes.sort();

    for (auto i : primes){
        cout << i <<" ";
    }
    cout <<endl << "Number of primes: " <<  primes.size() << endl;

    return 0;
}




