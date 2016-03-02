EXAMPLE_PROJECT=example-project
EXAMPLE_PROJECT_PATH=example_project

cd ~/projects/untangled/template

lein install &&
    rm -r $EXAMPLE_PROJECT &&
    lein new untangled $EXAMPLE_PROJECT -- :all &&
    cd $EXAMPLE_PROJECT &&
    ln -f resources/config/dev.edn /usr/local/etc/${EXAMPLE_PROJECT_PATH}.edn &&
    lein repl
