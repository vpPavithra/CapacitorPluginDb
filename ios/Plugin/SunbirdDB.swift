import Foundation

@objc public class SunbirdDB: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
