//
//  Kotlinx.swift
//  iosApp
//
//  Created by Marcin Siwak on 02/12/2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Foundation
import sharedFrontend
import ComposeApp
import Combine


extension Publisher {
    func asFlow<T>() -> Kotlinx_coroutines_coreFlow where Output == T, Failure == Never {
        return Kotlinx_coroutines_coreFlowAdapter(publisher: self)
    }
}
extension Publisher {
    func asSharedFlow<T>() -> Kotlinx_coroutines_coreSharedFlow where Output == T, Failure == Never {
        return Kotlinx_coroutines_coreSharedFlowAdapter(publisher: self)
    }
}

class Kotlinx_coroutines_coreFlowAdapter<T>: Kotlinx_coroutines_coreFlow {
    private var cancellable: AnyCancellable?
    private let publisher: AnyPublisher<T, Never>

    init<P: Publisher>(publisher: P) where P.Output == T, P.Failure == Never {
        self.publisher = publisher.eraseToAnyPublisher()
    }

    func collect(collector: Kotlinx_coroutines_coreFlowCollector, completionHandler: @escaping (Error?) -> Void) {
        cancellable = publisher.sink { value in
            collector.emit(value: value) { _ in }
        }
    }
}


class Kotlinx_coroutines_coreSharedFlowAdapter<T>: Kotlinx_coroutines_coreSharedFlow {
    var replayCache: [Any] = []
    
    private var cancellable: AnyCancellable?
    private let publisher: AnyPublisher<T, Never>

    init<P: Publisher>(publisher: P) where P.Output == T, P.Failure == Never {
        self.publisher = publisher.eraseToAnyPublisher()
    }

    func collect(collector: Kotlinx_coroutines_coreFlowCollector, completionHandler: @escaping (Error?) -> Void) {
        cancellable = publisher.sink { value in
            self.replayCache = [value]
            for item in self.replayCache {
                    collector.emit(value: item) { _ in }
                }
            collector.emit(value: value) { _ in }
        }
    }
}
