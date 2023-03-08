# ThreadWeb

ThreadWeb is a simple science project for testing the effect of multithreading on a web server. This project was entered into NEMS Science Fair in 2023.

## Building

If you want to build and run this project yourself, you can follow these instructions:

1. Download the latest version of Docker, JDK19, and Apache Ant
2. Download this repository, either by downloading/extracting a zip file or by running: `$ git clone https://github.com/Supernova9987/ThreadWeb.git` at the terminal
3. Enter the resulting folder, and run `$ ant` at the terminal to build both servers.

Then to run the experiment, complete these steps:

1. Run either one of the servers with one of these commands: <br>
    To run the multithreaded server, run `$ docker run -d -p 80:80 --name ThreadWeb-Multi threadweb-multiserver:latest` <br>
    To run the single threaded server, run `$ docker run -d -p 80:80 --name ThreadWeb-Single threadweb-singleserver:latest`
2. Run `$ ant execute-client` to complete a trial for the experiment (do 3 or more trials and average them)
3. Repeat step 1 and 2 with the other server
4. Run `$ ant clean` to remove build artifacts, and run `$ docker rmi threadweb-multiserver threadweb-singleserver` to remove the server artifacts
