FROM gitpod/workspace-full

# Install custom tools, runtime, etc.
RUN sudo apt update && sudo apt upgrade -y 
RUN sudo docker run --name=mysql-docker -d mysql/mysql-server:latest  
RUN sudo docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
#ENV ... 
