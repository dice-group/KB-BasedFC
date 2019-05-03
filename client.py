from nameko.standalone.rpc import ClusterRpcProxy

CONFIG = {'AMQP_URI': "amqp://guest:guest@localhost"}

def kstream():
    with ClusterRpcProxy(CONFIG) as rpc:
        result = rpc.kstream.stream(392035, 599, 2115741)

def klinker():
    with ClusterRpcProxy(CONFIG) as rpc:
        result = rpc.klinker.stream(392035, 599, 2115741)

if __name__ == '__main__':
    kstream()
    klinker()
