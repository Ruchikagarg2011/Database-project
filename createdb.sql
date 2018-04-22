CREATE TABLE YelpUser
(
    Yelping_Since VARCHAR2(30),
    FunnyVotes NUMBER,
    UsefulVotes NUMBER,
    CoolVotes NUMBER,
    ReviewCount NUMBER,
    UserName VARCHAR2(255),
    UserId VARCHAR2(50),
    Fans NUMBER,
    AverageStars FLOAT,
    UserType VARCHAR2(10),
    CONSTRAINT YelpUserId PRIMARY KEY (UserId)
);

CREATE TABLE Friends
(
    UserId VARCHAR2(30),
    FriendId VARCHAR2(30),
  CONSTRAINT FriendsId  PRIMARY KEY (UserId,FriendId),
  CONSTRAINT Fk_FriendsId  FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE
);

CREATE TABLE EliteYears
(
    UserId VARCHAR2(30),
    Elite NUMBER,
    
    CONSTRAINT EliteYearsId   PRIMARY KEY (UserId,Elite),
   CONSTRAINT Fk_EliteYearsId  FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE
);

CREATE TABLE Compliments(
UserId VARCHAR2(32),
ComplimentType VARCHAR2(30),
ComplimentValue integer,
CONSTRAINT Pk_Compliments PRIMARY KEY (UserId,ComplimentType,ComplimentValue),
CONSTRAINT Fk_Compliments FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE
);

CREATE TABLE Business
(
    BusinessId VARCHAR2(50),
    FullAddress VARCHAR2(255),
    IsOpen VARCHAR2(20),
    City VARCHAR2(255),
    ReviewCount NUMBER,
    BName VARCHAR2(255),
    Longitude FLOAT,
    State VARCHAR2(255),
    Stars FLOAT,
    Latitude FLOAT,
    BType VARCHAR2(50),
   CONSTRAINT Pk_Business PRIMARY KEY (BusinessId)
);

CREATE TABLE BusinessHours
(
    Days VARCHAR2(20),
    OpenHour VARCHAR2(20),
    CloseHour VARCHAR2(20),
    BusinessId VARCHAR2(50),
    CONSTRAINT Pk_BusinessHours PRIMARY KEY (BusinessId, Days),
    CONSTRAINT Fk_BusinessHours FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE Neighborhoods
(
	NeighborhoodName VARCHAR2(100),
	BusinessId VARCHAR2(50),
	CONSTRAINT Pk_Neighborhoods PRIMARY KEY (BusinessId, NeighborhoodName),
   CONSTRAINT Fk_Neighborhoods FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE BusinessAttributes
(
    BusinessId VARCHAR2(50),
    AttributeName VARCHAR2(225),
    AttributeValue VARCHAR2(255),
   CONSTRAINT Pk_BusinessAttributes PRIMARY KEY (BusinessId, AttributeName),
    CONSTRAINT Fk_BusinessAttributes FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE BusinessCategory
(
    CategoryName VARCHAR2(255),
    BusinessId VARCHAR2(50),
    CONSTRAINT Pk_BusinessCategory PRIMARY KEY (BusinessId, CategoryName),
    CONSTRAINT Fk_BusinessCategory FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE BusinessSubCategory
(   
    SubCategoryName VARCHAR2(255),
    BusinessId VARCHAR2(50),
     CONSTRAINT Pk_BusinessSubCategory PRIMARY KEY (BusinessId,SubCategoryName),
     CONSTRAINT Fk_BusinessSubCategory FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE   
);

CREATE TABLE CheckIN(
BusinessId VARCHAR2(50),
CheckinHour NUMBER,
CheckinDayNum NUMBER,
CheckinDay VARCHAR2(50),
CheckinCount NUMBER,
CONSTRAINT Pk_CheckIN PRIMARY KEY (BusinessId, CheckinHour,CheckinDay,CheckinCount),
CONSTRAINT Fk_CheckIN FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE
);

CREATE TABLE Review
(
    FunnyVote number,
    UsefulVote number,
    CoolVote number,
    TotalVotes number,
    UserId varchar2(50),
    ReviewId varchar2(50),
    Stars number,
    RDate varchar2(20),
    RText clob,
    RType varchar2(255),
    BusinessId varchar2(50),
   CONSTRAINT Pk_Review primary key (ReviewId),
    CONSTRAINT Fk1_Review FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE,
   CONSTRAINT Fk2_Review  FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE 
);

CREATE OR REPLACE Procedure YElP_Business_Review
( 
BId IN VARCHAR2,
BusinessReview OUT SYS_REFCURSOR

) AS
BEGIN
    
   OPEN BusinessReview FOR
    SELECT DISTINCT rev.ReviewId as ReviewId, b.BName as BName, dbms_lob.substr(rev.RText, 4000,1) as Review, yu.UserName as UserName, b.BusinessId as BusinessId
    FROM Business b, Review rev, YelpUser yu
    WHERE b.BusinessId = rev.BusinessId
    AND rev.UserId = yu.UserId
    AND rev.BusinessId = BId;
    
END YElP_Business_Review;
/
CREATE OR REPLACE Procedure YElP_User_Review
( 
YelpUserId IN VARCHAR2,
UserReview OUT SYS_REFCURSOR

) AS
BEGIN
    
   OPEN UserReview FOR
    SELECT DISTINCT rev.ReviewId as ReviewId, b.BName as BName, dbms_lob.substr(rev.RText, 4000,1) as Review, yu.UserName as UserName, b.BusinessId as BusinessId
    FROM Business b, Review rev, YelpUser yu
    WHERE b.BusinessId = rev.BusinessId
    AND rev.UserId = yu.UserId
    AND yu.UserId = YelpUserId;
    
END YElP_User_Review;
/
CREATE INDEX userIndex ON Friends(UserId);
CREATE INDEX UserAndReview ON YelpUser(UserId, ReviewCount);
CREATE INDEX userandAvgStar ON YelpUser(UserId, AverageStars);