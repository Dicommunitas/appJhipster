#FROM gitpod/workspace-full

RUN echo "----------------------------------"
RUN service --status-all
RUN sleep 3
RUN echo "----------------------------------"


# Install custom tools, runtime, etc.
#RUN sudo apt update && sudo apt upgrade -y 
RUN service docker start
RUN docker run --name=mysql-docker -d mysql/mysql-server:latest  
RUN docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
#ENV ... 
