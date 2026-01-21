#include <iostream>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
void server_run()
{
	sockaddr_in addr{};
	addr.sin_family = AF_INET;
	addr.sin.port = htons(port);
	addr.sin_addr.s_addr = INADDR_ANY;
	int server_socket = socket(AF_INET, SOCK_STREAM, 0);

	if (server_socket < 0) {
		perror("server socket");
		return;
	}

	if (bind(server_socket, (struct sockaddr *) &addr, sizeof(addr)) < 0) {
		perror("bind");
		close(server_socket);
		return;
	}

	if (listen(server_socket, 1) < 0) {
		perror("listen");
		close(server_socket);
		return;
	}

	while(true) {
		int client = accept(server_socket, nullptr, nullptr);
		if (client < 0) {
			perror("accept");
			continue;
		}
		uint32_t len;
		if (recv(client, &len, sizeof(len), MSG_WAITALL) <= 0) {
			close(client);
			continue;
		}
		len = ntohl(len);
		std::string msg(len, '\0');
		if (recv(client, msg.data(), len, MSG_WAITALL) > 0)
			std::cout << "[SERVER] " << msg << std::endl;
		close(client);
	}
}
