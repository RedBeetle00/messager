#include "server.h"
#include "notification.h"
#include <iostream>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <csignal>

void server_run(int port)
{
	const int MIN_PORT = 1024;
	const int MAX_PORT = 65535;

	if (port < MIN_PORT || port > MAX_PORT) {
		std::cout << "The port must be in the range from 1024 to 65535\n";
		return;
	}

	sockaddr_in addr{};
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr.s_addr = INADDR_ANY;
	int server_socket = socket(AF_INET, SOCK_STREAM, 0);

	if (server_socket < 0) {
		perror("server socket");
		return;
	}

	int opt = 1;
	setsockopt(server_socket, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

	if (bind(server_socket, (struct sockaddr *) &addr, sizeof(addr)) < 0) {
		perror("bind");
		close(server_socket);
		return;
	}

	if (listen(server_socket, SOMAXCONN) < 0) {
		perror("listen");
		close(server_socket);
		return;
	}
	
	std::cout << "Start on port " << port << std::endl;

	while(1) {
		int client = accept(server_socket, nullptr, nullptr);
		if (client < 0) {
			perror("accept");
			continue;
		}
		std::cout << "User connect\n";
		while(1) {
			char buffer[1024];
			ssize_t bytes_received = recv(client, buffer, sizeof(buffer) - 1, 0);
			if (bytes_received > 0) {
				buffer[bytes_received] = '\0';
				std::cout << buffer << std::endl;
				show_notify(buffer);
			}
			else if (bytes_received == 0) {
				std::cout << "User disconnect\n";
				break;
			}
			else {
				perror("recv");
				break;
			}
		}
		close(client);
	}
}
