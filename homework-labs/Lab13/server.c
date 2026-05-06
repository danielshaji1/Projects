#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        exit(1);
    }

    int port = atoi(argv[1]);

    int server_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (server_sock < 0) { perror("socket"); exit(1); }

    struct sockaddr_in server;
    memset(&server, 0, sizeof(server));
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = htonl(INADDR_ANY);

    if (bind(server_sock, (struct sockaddr*)&server, sizeof(server)) < 0) {
        perror("bind");
        close(server_sock);
        exit(1);
    }

    if (listen(server_sock, 1) < 0) {
        perror("listen");
        close(server_sock);
        exit(1);
    }

    int client_sock = accept(server_sock, NULL, NULL);
    if (client_sock < 0) {
        perror("accept");
        close(server_sock);
        exit(1);
    }

    char buffer[1024];
    int n = recv(client_sock, buffer, sizeof(buffer)-1, 0);
    if (n < 0) { perror("recv"); }
    else {
        buffer[n] = '\0';
        printf("The server received: %s\n", buffer);
    }

    close(client_sock);
    close(server_sock);
    return 0;
}
