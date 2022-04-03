#FROM gitpod/workspace-full


# Install custom tools, runtime, etc.
# RUN sudo apt update && sudo apt upgrade -y 
# RUN sudo service --status-all 
# RUN sudo docker-up
# RUN docker run --name=mysql-docker -d mysql/mysql-server:latest  
# RUN docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
# ENV...
ports:
  - port: 3000
    onOpen: open-preview
tasks:
  - before: if [[ -z "$experiment" ]]; then cd playground/1st-proof-of-concept; else cd playground/$experiment; fi
    command: nvm install 13.3.0 && npm install && npm start
