NAME=$1
LASTNAME=$2
SHOW=$3

if  [ "$SHOW" = "true"] ; then
  echo "hello, $NAME $LASTNAME"
else
 echo "if you want to see the name, please use the SHOW option"
fi
