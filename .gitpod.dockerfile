FROM gitpod/workspace-full

# Install custom tools, runtime, etc.
RUN sudo apt update && sudo apt upgrade -y \
    docker run --name=mysql-docker -d mysql/mysql-server:latest \
    docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
#ENV ... 
