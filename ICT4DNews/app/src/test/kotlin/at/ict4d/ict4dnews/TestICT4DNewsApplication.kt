package at.ict4d.ict4dnews

class TestICT4DNewsApplication : ICT4DNewsApplication() {

    override fun installLeakCanary() {
        // do not install LeakCanary in tests
    }

    override fun installStetho() {
        // do not install Stetho in tests
    }
}
