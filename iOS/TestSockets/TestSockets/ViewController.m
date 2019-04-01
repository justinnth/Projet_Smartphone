//
//  ViewController.m
//  TestSockets
//
//  Created by Justin North on 01/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self connectToServer];
}

- (void) connectToServer{
    NSLog(@"Connexion à 10.13.69.216:55555");
    CFStreamCreatePairWithSocketToHost(kCFAllocatorDefault, (__bridge CFStringRef) @"10.13.69.216", 55555, &readStream, &writeStream);
    
    messages = [[NSMutableArray alloc] init];
    
    [self open];
}

- (void) messageReceived:(NSString *)message {
    
    [messages addObject:message];
    
    if (![message hasPrefix:@"Simulator"]) {
        NSArray *dataArray = [message componentsSeparatedByString:@"\n"];
        NSArray *GPRMC = [dataArray[0] componentsSeparatedByString:@","];
        
        NSString *valide = GPRMC[2];
        if ([valide isEqualToString:@"A"]) {
            NSString *lat = GPRMC[3]; // String contenant les données de la latitude au format ddmm.mmmm
            
            NSArray *latData = [lat componentsSeparatedByString:@"."]; // Séparation par le point
            NSString *dm = latData[0];
            NSString *d = [dm substringToIndex:2];
            NSString *m = [dm substringFromIndex:2];
            NSString *s = latData[1];
            
            float latF = [d floatValue] + ([m floatValue]/60) + (([s floatValue]*60/100)/3600); // Calcul de la valeur de la latitude pour la map
            
            NSString *sLat = GPRMC[4];
            if ([sLat isEqualToString:@"S"])
                latF = -latF;
            
            NSString *lng = GPRMC[5]; // String contenant les données de la longitude au format dddmm.mmmm
            
            NSArray *lngData = [lng componentsSeparatedByString:@"."];
            NSString *dm2 = lngData[0];
            NSString *d2 = [dm2 substringToIndex:3];
            NSString *m2 = [dm2 substringFromIndex:3];
            NSString *s2 = lngData[1];
            
            float lngF = [d2 floatValue] + ([m2 floatValue]/60) + (([s2 floatValue]*60/100)/3600); // Calcul de la valeur de la longitude pour la map
            
            NSString *sLng = GPRMC[6];
            
            if ([sLng isEqualToString:@"W"])
                lngF = -lngF;
            
            _coordonnees.text = [NSString stringWithFormat:@"Latitude : %f\nLongitude : %f", latF, lngF];
            
            /**
             * Ajout de point à chaque coordonnées
             */
            MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(CLLocationCoordinate2DMake(latF, lngF), 800, 800);
            [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES];
            
            MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
            point.coordinate = CLLocationCoordinate2DMake(latF, lngF);
            
            [self.mapView addAnnotation:point];
            
            MKRoute *route;
            [self.mapView addOverlay:[route polyline] level:MKOverlayLevelAboveRoads];
            
            /**
             * Vitesse
             */
            NSString *vitesse = GPRMC[7];
            float v = [vitesse intValue]*4.98/2.69;
            
            _vitesse.text = [NSString stringWithFormat:@"Vitesse : %.02f km/h", v];
        } else{
            _coordonnees.text = @"Données invalides";
        }
    }
}

- (void)stream:(NSStream *)aStream handleEvent:(NSStreamEvent)eventCode{
    switch (eventCode) {
        case NSStreamEventOpenCompleted:
            NSLog(@"Stream ouvert");
            break;
            
        case NSStreamEventHasBytesAvailable:
            if (aStream == inputStream) {
                uint8_t buffer[1024];
                NSInteger len;
                
                while ([inputStream hasBytesAvailable]){
                    len = [inputStream read:buffer maxLength:sizeof(buffer)];
                    if (len > 0){
                        NSString *output = [[NSString alloc] initWithBytes:buffer length:len encoding:NSASCIIStringEncoding];
                        
                        if (nil != output)
                            [self messageReceived:output];
                    }
                }
            }
            break;
            
        case NSStreamEventHasSpaceAvailable:
            NSLog(@"Stream has space available now");
            break;
            
        case NSStreamEventErrorOccurred:
            NSLog(@"%@",[aStream streamError].localizedDescription);
            break;
            
        case NSStreamEventEndEncountered:
            [aStream close];
            [aStream removeFromRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
            NSLog(@"close stream");
            break;
            
        default:
            NSLog(@"Unknown event");
            break;
    }
}

- (void) open{
    outputStream = (__bridge NSOutputStream *)writeStream;
    inputStream = (__bridge NSInputStream *)readStream;
    
    [outputStream setDelegate:self];
    [inputStream setDelegate:self];
    
    [outputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
    [inputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
    
    [outputStream open];
    [inputStream open];
}


@end
