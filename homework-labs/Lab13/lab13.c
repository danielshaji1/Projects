#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>



/*
  This function takes command line arguments as <server_ip> <port> <message>
  and sends a message to a server,or itself as the server, that is listening
  on the same message and correct ip that the client is sending it to.
 */
int main(int argc, char *argv[]) {
    if (argc != 4) {
        fprintf(stderr, "Usage: %s <server_ip> <port> <message>\n", argv[0]);
        exit(1);
    }

    const char *ip = argv[1];
    int port = atoi(argv[2]);
    const char *msg = argv[3];

    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) { perror("socket"); exit(1); }

    // struct for the default server address parameters
    struct sockaddr_in server;
    memset(&server, 0, sizeof(server));
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = inet_addr(ip);

    if (connect(sock, (struct sockaddr*)&server, sizeof(server)) < 0) {
        perror("connect");
        close(sock);
        exit(1);
    }

    if (send(sock, msg, strlen(msg), 0) < 0) {
        perror("send");
        close(sock);
        exit(1);
    }

    close(sock);
    return 0;
}
