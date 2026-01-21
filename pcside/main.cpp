#include "connection/server.h"
#include <iostream>

int main(int argc, char* argv[])
{
	std::cout << "Start\n";
	server_run(8080);
	return 0;
}
