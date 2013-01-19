namespace java com.github.karan.test.gen

struct TestInfo {
    1: string message
}

service TestService {
    void add(1: TestInfo info),
    TestInfo get()
}