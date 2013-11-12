#Housing Design Document

##Landlord/Owner Agent
 
###Data
 
    PersonAgent me
    Timer rentTimer;
    SimCityGui gui
    List<Renter> RenterList;
    List<House> HousesList
    int mMinCreditScore Requirement;
    Enum RenterState{RentPaid, OwesRent, RentOverdue};
    Boolean TimeToCheckRent = false;
 
    Class Renter(){
            PersonAgent p;
            RenterState state;
        int creditscore; 
        House house; 
    }
 
 
###Messages

    msgIWouldLikeToLiveHere(Peasant p, int creditScore){
        RenterList.add(new Renter(p, creditScore));
    }
 
    msgHereIsBankStatement(int SSN, double paymentAmt){
        Renter r = FindRenter(SSN);
        r.state = RentPaid;
    }

 
 
###Scheduler

    If there exists Renter r in RenterList such that r.state = ApplyingForHousing then 
        ReviewApplicant(p);
 
    If(TimeToCheckRent) and there exists Renter r in RenterList {}
        TimeToCheckRent = false;
        entTimer.start()

        For each Renter r in RenterList
            If r.state = RentOverdue then GiveEvictionNotice(r);

        For each Renter r in RenterList
            If r.state = OwesRent then GiveRentOverdueNotice(r);

        For each Renter r in RenterList
            If r.state = RentPaid then GiveRentDueNotice(r); 
    }

 

###Actions

    ReviewApplicant(Renter r){
        If(r.creditscore >= minCreditScore)
            r.house =HouseList.house; //TO BE IMPLEMENTED 
            r.house.occupant = r.p;
            r.p.msgApplicationAccepted(r.house);
            r.state = RentPaid;
        else
            r.p.msgApplicationDenied();
            RenterList.remove(r);
    }

    GiveRentDueNotice(Renter r){
        r.state = OwesRent;
        r.p.msgRentDue(this, r.house.rent); 
    }

    GiveRentOverdueNotice(Renter r){
        r.state = RentOverdue
        r.p.msgRentOverdueNotice(this, r.house.rent);
    }

    GiveEvictionNotice(Renter r){
        r.p.msgEviction(); 
        HousesList.get(r.house).occupant = null 
        RenterList.remove(r); 
    }



##Real Estate

    class House {
        int x, y location //for animating
        double rent
        PersonAgent occupant 
    } 


##Agent: HouseRenter

###Data

    enum BillStates { Pending, Paid }
    PersonAgent me
    LandLord myLandLord
    Class Bill {
        LandLord mLandLord
        double amt
        BillState status = Pending
    }
    House mHouse = null
    Timer maintainTimer;
    Boolean TimeToMaintain = false;
    List<Bill> bills


###Actions

    RequestHousing()
        msgIWouldLikeToLiveHere(me, me.mCreditScore)

    PayBill(Bill b)
        me.bank.msgSendPayment(this, b.mLandLord, b.amt); 
        bills.remove(b)
    Maintain()
        //run timer for some period of time, animate
        h.timeSinceLastMaintenance = 0;


###Messages

    msgApplicationAccepted(House newHouse)
        mHouse = newHouse

    msgApplicationDenied()
        mHouse = null

    msgRentDue(Landlord l, double total)
        bills.add(new Bill(l, total))

    msgOverdueNotice(Landlord l, double total)
        bills.add(new Bill(l, total)
    
    msgEviction()
        mHouse = null
        DoLeaveHouse() //Some eviction animation

    msgLease(Landlord l, House h)
       mHouse = h;
       //some exchange of money


###Scheduler

    if (mHouse!=null)
        If there exists a Bill b in bills such that b.status == Pending
        PayBill(Bill b)

    if (TimeToMaintain)
        TimeToMaintain = false;
        maintainTimer.start()
        Maintain()
