FROM gcc:latest

COPY . /usr/src/cpp
WORKDIR /usr/src/cpp
RUN g++ -o cpp cppFile.cpp
CMD ["./cpp"]