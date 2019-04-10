//
//  ViewController.m
//  Projet_Smartphone_Final
//
//  Created by Justin North on 09/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    routeLocations = [[NSMutableArray alloc] init]; // Tableau pour stocker les NSDictionnary qui contiendront les coordonnées
    [self connectToServer]; // Connexion au serveur NMEA Simulator
}

- (void) connectToServer{
    NSLog(@"Connexion à 192.168.0.103:55555");
    CFStreamCreatePairWithSocketToHost(kCFAllocatorDefault, (__bridge CFStringRef) @"192.168.0.103", 55555, &readStream, &writeStream); // Création de la connexion au socket
    
    messages = [[NSMutableArray alloc] init]; // Tableau pour stocker les données NMEA
    
    [self open]; // Ouverture du stream pour la connexion au serveur
}

- (void) messageReceived:(NSString *)message {
    
    [messages addObject:message];
    
    if (![message hasPrefix:@"Simulator"]) {
        NSArray *dataArray = [message componentsSeparatedByString:@"\n"]; // Récupération des données et séparation par \n
        NSArray *GPRMC = [dataArray[0] componentsSeparatedByString:@","]; // On a donc la trame GPRMC des données NMEA, on les sépare par la virgule
        
        NSString *valide = GPRMC[2];
        /**
         * Si les données sont valides
         */
        if ([valide isEqualToString:@"A"]) {
            NSString *latT = GPRMC[3]; // String contenant les données de la latitude au format ddmm.mmmm
            
            NSArray *latData = [latT componentsSeparatedByString:@"."]; // Séparation par le point
            NSString *dm = latData[0];
            NSString *d = [dm substringToIndex:2];
            NSString *m = [dm substringFromIndex:2];
            NSString *s = latData[1];
            
            float latF = [d floatValue] + ([m floatValue]/60) + (([s floatValue]*60/100)/3600); // Calcul de la valeur de la latitude en DD pour la map
            
            NSString *sLat = GPRMC[4]; // Récupération de N ou S pour le -
            if ([sLat isEqualToString:@"S"])
                latF = -latF;
            
            NSString *lngT = GPRMC[5]; // String contenant les données de la longitude au format dddmm.mmmm
            
            NSArray *lngData = [lngT componentsSeparatedByString:@"."]; // Séparation par le point
            NSString *dm2 = lngData[0];
            NSString *d2 = [dm2 substringToIndex:3];
            NSString *m2 = [dm2 substringFromIndex:3];
            NSString *s2 = lngData[1];
            
            float lngF = [d2 floatValue] + ([m2 floatValue]/60) + (([s2 floatValue]*60/100)/3600); // Calcul de la valeur de la longitude en DD pour la map
            
            NSString *sLng = GPRMC[6]; // Récupération de E ou W pour le -
            if ([sLng isEqualToString:@"W"])
                lngF = -lngF;
            
            _latitude.text = [NSString stringWithFormat:@"Latitude : %f", latF]; // Affichages des coordonnées dans la text view
            _longitude.text = [NSString stringWithFormat:@"Longitude : %f", lngF];
            
            /**
             * Conversion en NSNumber pour utiliser le NSDictionary pour les lignes
             */
            NSNumber *lat = [NSNumber numberWithFloat:latF];
            NSNumber *lng = [NSNumber numberWithFloat:lngF];
            
            NSDictionary *coordonnees = @{ @"lat" : lat, @"lon" : lng }; // Création du NSDictionary
            
            [routeLocations addObject:coordonnees]; // Ajout des coordonnées dans le tableau
            
            
            MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(CLLocationCoordinate2DMake(latF, lngF), 200, 200);
            [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES]; // Placement de la caméra sur le point en cours
            
            CLLocationCoordinate2D routeCoord[routeLocations.count]; // Création d'un tableau de CLLocationCoordiante2D
            for (int i = 0; i < routeLocations.count; i++) {
                id location = [routeLocations objectAtIndex:i];
                routeCoord[i] = CLLocationCoordinate2DMake([[location objectForKey:@"lat"] floatValue], [[location objectForKey:@"lon"] floatValue]); // Remplissage du tableau
            }
            
            MKPolyline *poly = [MKPolyline polylineWithCoordinates:routeCoord count:routeLocations.count];
            [self.mapView addOverlay:poly]; // Création des lignes
            _mapView.delegate = self;
            
            /**
             * Vitesse
             */
            NSString *vitesse = GPRMC[7];
            float v = [vitesse intValue]*4.98/2.69;
            
            _vitesse.text = [NSString stringWithFormat:@"%.02f km/h", v]; // Affichage de la vitesse dans la text view
        } else{
            _latitude.text = @"Données invalides";
        }
    }
}

- (void)stream:(NSStream *)aStream handleEvent:(NSStreamEvent)eventCode{
    switch (eventCode) {
            
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
            
        case NSStreamEventErrorOccurred:
            NSLog(@"%@",[aStream streamError].localizedDescription);
            break;
            
        case NSStreamEventEndEncountered:
            [aStream close];
            [aStream removeFromRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
            NSLog(@"Fermeture du stream");
            break;
            
        default:
            //NSLog(@"Evenement inconnu");
            break;
    }
}

/**
 * Ouverture du stream pour la connexion au simulateur
 */
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

/**
 * Renderer pour les lignes entre les points
 */
- (MKOverlayRenderer *)mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay{
    if ([overlay isKindOfClass:[MKPolyline class]]) {
        MKPolylineRenderer *routeRenderer = [[MKPolylineRenderer alloc] initWithPolyline:overlay];
        routeRenderer.strokeColor = [UIColor redColor];
        routeRenderer.lineWidth = 4;
        return routeRenderer;
    }
    return nil;
}



@end
