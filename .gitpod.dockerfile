#FROM gitpod/workspace-full


# Install custom tools, runtime, etc.
# RUN sudo apt update && sudo apt upgrade -y 
# RUN sudo service --status-all 
# RUN sudo docker-up
# RUN docker run --name=mysql-docker -d mysql/mysql-server:latest  
# RUN docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
# ENV...

# This configuration is intended for development purpose, it's **your** responsibility to harden it for production
version: '3.8'
services:
  controleoperacional-mysql:
    image: mysql:8.0.28
    # volumes:
    #   - ~/volumes/jhipster/controleOperacional/mysql/:/var/lib/mysql/
    environment:
      - MYSQL_ALLOW_EMPTY_PASSWORD=yes
      - MYSQL_DATABASE=controleoperacional
    # If you want to expose these ports outside your dev PC,
    # remove the "127.0.0.1:" prefix
    ports:
      - 127.0.0.1:3306:3306
    command: mysqld --lower_case_table_names=1 --skip-ssl --character_set_server=utf8mb4 --explicit_defaults_for_timestamp
