//
//  ViewController.swift
//  Projet_Smartphone
//
//  Created by Justin North on 25/03/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    -(UIInterfaceOrientation)preferredInterfaceOrientationForPresentation{
    return UIInterfaceOrientationLandscapeLeft  || UIInterfaceOrientationLandscapeRight ;
    }
    
    -(UIInterfaceOrientationMask)supportedInterfaceOrientation{
    return UIInterfaceOrientationMaskLandscape;
    }

}
