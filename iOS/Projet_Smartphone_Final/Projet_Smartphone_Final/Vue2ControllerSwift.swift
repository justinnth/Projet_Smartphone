//
//  Vue2ControllerSwift.swift
//  Projet_Smartphone_Final
//
//  Created by Justin North on 09/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

import Foundation
import UIKit
import MapKit
import CoreMotion

@objc class Vue2ControllerSwift: UIViewController, MKMapViewDelegate {
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var home: UIButton!
    @IBOutlet weak var urgence: UIButton!
    
    var motionManager: CMMotionManager!
    
    let homeLat = 46.147248
    let homeLon = -1.168250
    
    let bateau = MKPointAnnotation()
    
    var coordonnees: Array<CLLocationCoordinate2D> = Array()
    
    var vitesse = 0.0
    var barre = 0.0
    var angle = 0.0
    var latBateau = 0.0
    var lonBateau = 0.0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.home.backgroundColor = UIColor.green // Couleur du bouton HOME
        self.urgence.backgroundColor = UIColor.red // Couleur du bouton URGENCE
        
        self.motionManager = CMMotionManager() // Motion manager pour l'accelerometre
        
        self.latBateau = self.homeLat
        self.lonBateau = self.homeLon
        
        self.initPosition()
        self.startAccelerometre()
    }
    
    func initPosition() {
        if self.motionManager.isAccelerometerAvailable{
            self.motionManager.stopAccelerometerUpdates()
        }
        self.bateau.title = "HOME"
        self.bateau.coordinate = CLLocationCoordinate2D(latitude: self.homeLat, longitude: self.homeLon)
        self.mapView.addAnnotation(self.bateau)
        self.mapView.setRegion(MKCoordinateRegion(center: self.bateau.coordinate, latitudinalMeters: 800, longitudinalMeters: 800), animated: true)
    }
    
    func startAccelerometre() {
        print("Accelerometre declenché")
        //Setup de l'accéléromètre
        if self.motionManager.isAccelerometerAvailable {
            self.motionManager.accelerometerUpdateInterval = 1.0/60.0
            self.motionManager.startAccelerometerUpdates(to: OperationQueue.current!){(data, error)in
                if let myData = data{
                    if(myData.acceleration.z <= -0.5){
                        self.accelerer()
                    } else if (myData.acceleration.z < 0 && myData.acceleration.z > -0.5){
                        print("else if")
                    } else {
                        self.ralentir()
                    }
                    
                    if(myData.acceleration.y >= 0.35){
                        self.droite()
                    } else if (myData.acceleration.y > -0.35 && myData.acceleration.y < 0.35){
                        print("else if 2")
                    } else {
                        self.gauche()
                    }
                }
                self.updateAnnotation()
            }
        }
    }
    
    func accelerer() {
        print("Accelere")
        if self.vitesse < 25{
            self.vitesse += 0.1
        }
    }
    
    func ralentir() {
        print("Ralenti")
        vitesse -= 0.2
        if vitesse < 0 {
            vitesse = 0
        }
    }
    
    func gauche() {
        print("Tourne à gauche")
        self.barre += 0.01
        self.angle += 0.01
        
        if(self.barre > 30.0){
            self.barre = 30.0
        }
        
        if(self.angle < 0){
            self.angle +=  360
        } else if(self.angle > 360){
            self.angle -= 360
        }
    }
    
    func droite() {
        print("Tourne à droite")
        self.barre -= 0.01
        self.angle -= 0.01
        
        if(self.barre < -30.0){
            self.barre = -30.0
        }
        
        if(self.angle < 0){
            self.angle +=  360
        } else if(self.angle > 360){
            self.angle -= 360
        }
    }
    
    func updateAnnotation() {
        print("Deplacement")
        let distanceParcourue = self.vitesse/0.08
        let diffX = sin(barre) * distanceParcourue * 1.0/120.0
        let diffY = cos(barre) * distanceParcourue * 1.0/120.0
        
        self.latBateau += (diffX / 110574.61)
        self.lonBateau += (diffY / 110574.61)
        
        self.bateau.coordinate = CLLocationCoordinate2D(latitude: latBateau, longitude: lonBateau)
        let locationHome = CLLocationCoordinate2DMake(latBateau, lonBateau)
        self.mapView.setRegion(MKCoordinateRegion(center: locationHome, latitudinalMeters: 500, longitudinalMeters: 500), animated: false)
        
        let currentCoordinates = CLLocationCoordinate2DMake(latBateau, lonBateau)
        self.coordonnees.append(currentCoordinates)
        
        let line = MKPolyline(coordinates: coordonnees, count: coordonnees.count)
        self.mapView.addOverlay(line)
        mapView.delegate = self
    }
    
    // Permet de repositionner le marqueur au lieu de départ
    @IBAction func onClick(_ sender: UIButton) {
        self.initPosition()
    }
    
    // Permet de stopper le drone sur place et de stopper l'accelerometre
    @IBAction func onClickUrgence(_ sender: UIButton) {
        self.vitesse = 0
        self.updateAnnotation()
        self.motionManager.stopAccelerometerUpdates()
    }
    
    // Stop l'accelerometre lorsque l'on change de vue
    @IBAction func stopAccelerometre(_ sender: UIButton) {
        self.motionManager.stopAccelerometerUpdates()
    }
    
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        if overlay is MKPolyline {
            let routeRenderer = MKPolylineRenderer(overlay: overlay)
            routeRenderer.strokeColor = UIColor.red
            routeRenderer.lineWidth = 4
            return routeRenderer
        }
        return MKOverlayRenderer()
    }
    
    /**
     * Fonctions permettant de forcer le passage en mode paysage pour controler le drone
     */
    override var shouldAutorotate: Bool{
        return false
    }
    
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask{
        return UIInterfaceOrientationMask.landscapeLeft
    }
    
    override var preferredInterfaceOrientationForPresentation: UIInterfaceOrientation{
        return UIInterfaceOrientation.landscapeLeft
    }
    
}
