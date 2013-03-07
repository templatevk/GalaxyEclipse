#!/bin/bash
protoc -I="protocol" --java_out="src/main/java" protocol/messages.proto
