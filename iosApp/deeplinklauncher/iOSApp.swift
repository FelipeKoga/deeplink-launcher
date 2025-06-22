import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        AppInitializer.shared.doInit()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().edgesIgnoringSafeArea(.all)
        }
    }
    
}
