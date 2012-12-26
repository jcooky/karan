namespace java com.github.karan.gateway.gen

exception InvalidExcuteException {
	1: string why
}

service Gateway {
	string getService(1: string uri),
	oneway void put(1: string uri, 2: binary impls),
	oneway void update(1: string uri, 2: binary impls),
	oneway void remove(1: string uri),
	binary execute(1: string uri, 2: binary thriftBinaries) throws (1: InvalidExcuteException e)
}