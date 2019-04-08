//
//  Vue2Controller.h
//  TestSockets
//
//  Created by Justin North on 08/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface Vue2Controller : UIViewController <MKMapViewDelegate>{
    
}

@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIButton *home;
@property (weak, nonatomic) IBOutlet UIButton *urgence;



@end

