#!/bin/bash
cat create.v8.sql | mysql -u root && cat functions.sql | mysql -u root && cat procedures.sql | mysql -u root && cat insert.sql | mysql -u root
