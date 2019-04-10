//
//  Vue3Controller.h
//  Projet_Smartphone_Final
//
//  Created by Justin North on 09/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface Vue3Controller : UIViewController<MKMapViewDelegate, MKAnnotation>{
    IBOutlet UITextView * CoordonneesBateau;
    IBOutlet UITextView * CoordonneesPoints;
    IBOutlet UITextView * Vitesse;
    IBOutlet MKMapView * carte;
    IBOutlet UIButton * clearTrace;
    NSMutableArray<NSValue*> * mapPointArray;
    MKPointAnnotation * an;
    MKPolyline * polyline;
}

@property(retain, nonatomic) MKMapView * carte;
@property(nonatomic,strong) IBOutlet UITextView * CoordonneesBateau;
@property(nonatomic,strong) IBOutlet UITextView * CoordonneesPoints;
@property(nonatomic,strong) IBOutlet UITextView * Vitesse;
@property(nonatomic,retain) MKPolyline * polyline;
@property(nonatomic,retain) MKPointAnnotation * an;
@property(nonatomic,strong) IBOutlet UIButton * clearTrace;
@property(nonatomic,strong)NSMutableArray<NSValue*> *mapPointArray;

@end
