//
//  SwiftLibDependencyFactoryImpl.swift
//  iosApp
//
//  Created by Marcin Siwak on 24/08/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import ComposeApp

class DIProviderImpl: DIProvider {
    func provideConnectionManager() -> any ConnectionManager {
        return ConnectionManagerImpl()
    }
    
    
    static var shared = DIProviderImpl()

    func provideKtorServerImpl() -> any KtorServer {
        return KtorServerImpl()
    }
}
