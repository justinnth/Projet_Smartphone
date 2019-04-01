//
//  ViewController.h
//  TestSockets
//
//  Created by Justin North on 01/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface ViewController : UIViewController <NSStreamDelegate, MKMapViewDelegate>{
    CFReadStreamRef readStream;
    CFWriteStreamRef writeStream;
    
    NSInputStream *inputStream;
    NSOutputStream *outputStream;
    
    NSMutableArray *messages;
}

@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UITextView *vitesse;
@property (weak, nonatomic) IBOutlet UITextView *coordonnees;


@end

