#import <Foundation/Foundation.h>

#import <React/RCTViewManager.h>

@interface CastButtonManager : RCTViewManager
@end

@implementation CastButtonManager

RCT_EXPORT_MODULE()

RCT_EXPORT_VIEW_PROPERTY(tintColor, UIColor)

- (UIView *)view
{
  CGRect frame = CGRectMake(0, 0, 24, 24);
  UIView *castButton = [[UIView alloc] initWithFrame:frame];
  castButton.tintColor = [UIColor whiteColor];
  return castButton;
}

@end
