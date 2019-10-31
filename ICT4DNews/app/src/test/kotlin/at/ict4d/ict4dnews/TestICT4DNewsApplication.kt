package at.ict4d.ict4dnews

class TestICT4DNewsApplication : ICT4DNewsApplication() {

    override fun installStetho() {
        // do not install Stetho in tests
    }
}
