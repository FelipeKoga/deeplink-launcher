import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        KoinInitKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().edgesIgnoringSafeArea(.all)
        }
    }
    
}
