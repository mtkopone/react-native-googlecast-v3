#import <Foundation/Foundation.h>

#import <React/RCTViewManager.h>
#import <GoogleCast/GoogleCast.h>

@interface CastButtonManager : RCTViewManager
@end

@implementation CastButtonManager

RCT_EXPORT_MODULE()

RCT_EXPORT_VIEW_PROPERTY(tintColor, UIColor)

- (UIView *)view
{
  CGRect frame = CGRectMake(0, 0, 24, 24);
  GCKUICastButton *castButton = [[GCKUICastButton alloc] initWithFrame:frame];
  castButton.tintColor = [UIColor whiteColor];
  return castButton;
}

@end
