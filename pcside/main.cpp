#include "server.h"
#include <iostream>

int main(int argc, char* argv[])
{
	if (argc < 2) {
		std::cout << "Enter the port as the second argument\n";
		return 0;
	}
	server_run(atoi(argv[1]));
	return 0;
}
