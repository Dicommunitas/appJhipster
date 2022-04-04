#FROM gitpod/workspace-full
#FROM gitpod/workspace-full:latest
#FROM jhipster/jhipster:latest 

#Current default time zone: 'America/Sao_Paulo'
#sudo apt update
#lrwxrwxrwx 1 root root 37 Apr  4 04:21 /etc/localtime -> /usr/share/zoneinfo/America/Sao_Paulo
#sudo apt upgrade
#sudo apt install mysql-server
#sudo /etc/init.d/mysql stop
#sudo mysqld --skip-grant-tables - skip-networking &



# Install custom tools, runtime, etc.
#USER root
#RUN sudo apt update && apt upgrade -y 
#RUN service --status-all 
#USER gitpod
# RUN sudo docker-up
#RUN docker run --name=mysql-docker -d mysql/mysql-server:latest  
#RUN docker run --name=jhipster-docker -d jhipster/jhipster:latest 
    
# Apply user-specific settings
# ENV...

# Install postgres
#USER root
#RUN apt-get update && apt-get install postgresql postgresql-contrib -y
#RUN apt-get clean && rm -rf /var/cache/apt/* && rm -rf /var/lib/apt/lists/* && rm -rf /tmp/*

# Setup postgres server for user gitpod
#USER gitpod
#ENV PATH="/usr/lib/postgresql/10/bin:$PATH"
#RUN mkdir -p ~/pg/data 
#RUN mkdir -p ~/pg/scripts 
#RUN mkdir -p ~/pg/logs 
#RUN mkdir -p ~/pg/sockets 
#RUN initdb -d pg/data/
#RUN echo '#!/bin/bash\npg_ctl -D ~/pg/data/ -l ~/pg/logs/log -o "-k ~/pg/sockets" start' > ~/pg/scripts/pg_start.sh
#RUN echo '#!/bin/bash\npg_ctl -D ~/pg/data/ -l ~/pg/logs/log -o "-k ~/pg/sockets" stop' > ~/pg/scripts/pg_stop.sh
#RUN chmod +x ~/pg/scripts/*
#ENV PATH="$HOME/pg/scripts:$PATH"

# Project specifics
# Setup diesel_cli
#ENV PATH="$HOME/.cargo/bin:$PATH"
#RUN cargo install diesel_cli --no-default-features --features postgres

# Some transitive dependencies are very picky: We need the nightly build build on the 2018-04-14, meant for the 2018-04-15
#RUN rustup default nightly-2018-04-15
# Set some environment variables
#ENV DATABASE_URL=postgres://gitpod@127.0.0.1/rust-web-with-rocket
#ENV ROCKET_ADDRESS=0.0.0.0
#ENV ROCKET_PORT=8000

# Give back control
#USER root
&& 
