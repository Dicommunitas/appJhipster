FROM gitpod/workspace-full


# Install custom tools, runtime, etc.
#RUN sudo apt update && sudo apt upgrade -y 
RUN sudo docker-up
RUN docker run --name=mysql-docker -d mysql/mysql-server:latest  
RUN docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
#ENV ... 
