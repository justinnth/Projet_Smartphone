//
//  Simulation.h
//  Projet_Smartphone
//
//  Created by Dimitri Trichard on 26/03/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>


@interface Simulation : UIViewController<MKMapViewDelegate>{
    
    MKMapView *carte;
    UIButton *PilotageButton;
    UIButton *WaypointsButton;
    TextView *Vitesse;
    TextView *Coordonnees;
    
}

@property(nonatomic,weak)IBOutlet MKMapView *carte;
@property(nonatomic,weak)IBAction IBButton *PilotageButton;
@property(nonatomic,weak)IBAction IBButton *PWaypointsButton;
@property(nonatomic,weak)IBOutlet TextView *Vitesse;
@property(nonatomic,weak)IBOutlet TextView *Coordonnees;




@end

