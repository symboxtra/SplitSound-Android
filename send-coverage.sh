#!/bin/bash

#########################################################
#                                                       #
#            Upload code coverage reports               #
#                                                       #
#########################################################

if [ -f ./tokens.sh ]; then
    source ./tokens.sh
fi

bash <(curl -s https://codecov.io/bash) -t $CODE_COV_TOKEN
