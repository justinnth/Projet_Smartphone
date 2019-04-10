//
//  Vue3Controller.m
//  Projet_Smartphone_Final
//
//  Created by Justin North on 09/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import "Vue3Controller.h"

@interface Vue3Controller ()

@end

@implementation Vue3Controller
@synthesize carte, CoordonneesBateau, CoordonneesPoints, Vitesse, mapPointArray, an, polyline, clearTrace;

- (void)viewDidLoad {
    [super viewDidLoad];
    carte.delegate=self;
    self.carte.showsUserLocation=YES; self.carte.delegate=self;
    MKCoordinateRegion temp;
    temp.center.latitude=46.1504;
    temp.center.longitude=-1.16783;
    temp.span.latitudeDelta=0.01;
    temp.span.longitudeDelta=0.01;
    [carte setRegion: temp animated:YES];
    UITapGestureRecognizer *fingerTap = [[UITapGestureRecognizer alloc]
                                         initWithTarget:self action:@selector(handleMapFingerTap:)];
    fingerTap.numberOfTapsRequired = 1;
    fingerTap.numberOfTouchesRequired = 1;
    [self.carte addGestureRecognizer:fingerTap];
    mapPointArray = [[NSMutableArray alloc]init];
    polyline= [[MKPolyline alloc]init];
    an=[[MKPointAnnotation alloc]init];
}

- (IBAction)clearTrace:(UIButton *)sender {
    [self.carte removeOverlays:carte.overlays];
    [self.carte removeAnnotations:carte.annotations];
    [mapPointArray removeAllObjects];
}

- (void)handleMapFingerTap:(UIGestureRecognizer *)gestureRecognizer {
    if (gestureRecognizer.state != UIGestureRecognizerStateEnded) {
        return;
    }
    
    CGPoint touchPoint = [gestureRecognizer locationInView:self.carte];
    CLLocationCoordinate2D touchMapCoordinate =
    [self.carte convertPoint:touchPoint toCoordinateFromView:self.carte];
    
    CLLocation *location = [[CLLocation alloc] initWithLatitude:touchMapCoordinate.latitude longitude:touchMapCoordinate.longitude];
    
    MKPointAnnotation *annotationPoint = [[MKPointAnnotation alloc] init];
    annotationPoint.coordinate = location.coordinate;
    annotationPoint.title=@" ";
    CoordonneesPoints.text=[NSString stringWithFormat:@"Coordonnées du Points:\n Lat:%f Lon:%f", annotationPoint.coordinate.latitude,annotationPoint.coordinate.longitude];
    
    
    
    NSValue *val = [NSValue valueWithMKCoordinate:touchMapCoordinate];
    [mapPointArray addObject:val];
    [self.carte addAnnotation: annotationPoint];
    
    
    CLLocationCoordinate2D coordinates[[mapPointArray count]];
    if([mapPointArray count] != 1){
        for (NSInteger i=0; i<[mapPointArray count]; i++) {
            
            NSInteger j=i-1;
            CLLocationCoordinate2D coord = mapPointArray[i].MKCoordinateValue;
            
            coordinates[i] = coord;
            if(i==0){
                NSLog(@"i=1");
            }else {
                [self plotRouteOnMap:coordinates[j] atCurrent2DLocation:coordinates[i]];
            }
        }
    }
    if([mapPointArray count] == 1){
        CLLocationCoordinate2D coord = mapPointArray[0].MKCoordinateValue;
        coordinates[0] = coord;
        
    }
    
}

- (void)plotRouteOnMap: (CLLocationCoordinate2D )lastLocation atCurrent2DLocation: (CLLocationCoordinate2D )currentLocation {
    
    //Plot Location route on Map
    CLLocationCoordinate2D *plotLocation = malloc(sizeof(CLLocationCoordinate2D) * 2);
    plotLocation[0] = lastLocation;
    plotLocation[1] = currentLocation;
    MKPolyline *line = [MKPolyline polylineWithCoordinates:plotLocation count:2];
    [self.carte addOverlay:line];
}

- (MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id<MKOverlay>)overlay
{
    if([overlay isKindOfClass:[MKPolyline class]])
    {
        MKPolylineView *lineView = [[MKPolylineView alloc] initWithPolyline:overlay];
        lineView.lineWidth = 1;
        lineView.strokeColor = [UIColor redColor];
        lineView.fillColor = [UIColor redColor];
        return lineView;
    }
    return nil;
}

- (void)mapView: (MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    MKCoordinateRegion region=carte.region;
    CoordonneesBateau.text=[NSString stringWithFormat:@"Coordonnées du bateau:\nLatitude: %f\nLongitude: %f", region.center.latitude, region.center.longitude];
}

@end
