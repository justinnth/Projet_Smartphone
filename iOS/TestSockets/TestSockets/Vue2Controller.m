//
//  Vue2Controller.m
//  TestSockets
//
//  Created by Justin North on 08/04/2019.
//  Copyright © 2019 Justin North. All rights reserved.
//

#import "Vue2Controller.h"

@interface Vue2Controller ()

@end

@implementation Vue2Controller
@synthesize home, mapView, urgence;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.home setBackgroundColor:[UIColor greenColor]];
    [self.urgence setBackgroundColor:[UIColor redColor]];
}

- (BOOL)shouldAutorotate{
    return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskLandscapeLeft;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation{
    return UIInterfaceOrientationLandscapeLeft;
}

@end
