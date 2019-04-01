//
//  ViewController.swift
//  Projet_Smartphone
//
//  Created by Justin North on 25/03/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController {
    @IBOutlet weak var vitesse: UITextView!
    
    let hote = "127.0.0.1"
    let port = 55555
    var client: TCPClient?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        client = TCPClient(address: hote, port: Int32(port))
        getData()
    }
    
    private func getData(){
        guard let client = client else { return }
        
        switch client.connect(timeout: 10) {
        case .success:
            appendToTextField(string: "Connecté à l'hôte : \(client.address)")
            if let response = sendRequest(string: "GET / HTTP/1.1\n\n", using: client){
                if response.contains("$GPGLL"){
                    print("Trouvé")
                }
                appendToTextField(string: "Réponse : \(response)")
            }
        case .failure(let error):
            appendToTextField(string: String(describing: error))
        }
    }
    
    private func sendRequest(string: String, using client: TCPClient) -> String? {
        appendToTextField(string: "Envoi de données ... ")
        
        switch client.send(string: string) {
        case .success:
            return readResponse(from: client)
        case .failure(let error):
            appendToTextField(string: String(describing: error))
            return nil
        }
    }
    
    private func readResponse(from client: TCPClient) -> String? {
        guard let response = client.read(1024*10) else { return nil }
        
        return String(bytes: response, encoding: .utf8)
    }

    private func appendToTextField(string: String) {
        print(string)
        vitesse.text = vitesse.text.appending("\n\(string)")
    }
    
    func separateByString(String wholeString: String, byChar char:String) -> [String] {
        
        let resultArray = wholeString.components(separatedBy: char)
        return resultArray
    }
}

