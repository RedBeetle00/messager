CXX	 = g++
CXXFLAGS = -Wall -Wextra -O2 -std=c++20
LDFLAGS  =

CXXFLAGS += $(shell pkg-config --cflags libnotify)
LDFLAGS += $(shell pkg-config --libs libnotify)

TARGET   = messager
BUILD_DIR = build

SRCS 	 := $(wildcard *.cpp)
OBJS 	 := $(patsubst %.cpp,$(BUILD_DIR)/%.o,$(SRCS))

.PHONY: all clean

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) $(OBJS) -o $@ $(LDFLAGS)

$(BUILD_DIR)/%.o: %.cpp | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR) $(TARGET)
