#Housing Design Document

##Landlord/Owner Agent
 
###Data
 
    Timer timer;
    List<Peasant>Applicants;
    List<Renter> RenterList;
    Final int minCreditScore;
    Enum RenterState{ApplyingForHousing, RentPaid, OwesRent, RentOverdue, ToBeEvicted};
    Boolean TimeToCheckRent = false;
 
    Class Renter(){
        Peasant p;
        RenterState state;
    }
 
 
###Messages

    msgIWouldLikeToLiveHere(Peasant p, int creditScore){
        RenterList.add(new Renter(p, creditScore));
    }
 
    msgHereIsRent(Peasant p, Double rent){
        Renter r = FindRenter(p);
        r.state = RentPaid;
    }
 
 
###Scheduler

    If there exists Renter r in RenterList such that r.state = ApplyingForHousing then ReviewApplicant(p);
 
 
    If(TimeToCheckRent) and there exists Renter r in RenterList
        For each Renter r in RenterList
            If r.state = OwesRent then GiveRentOverdueNotice(r);
 
    For each Renter r in RenterList such that state = OwesRent then GiveRentOverdueNotice(r);
 
    For each Renter r in RenterList such that state = RentOverdue then GiveEvictionNotice(r);
 
    For each Renter r in RenterList such that state = ToBeEvicted then EvictRenter(r); 
 

###Actions

    ReviewApplicant(Renter r){
        If(r.creditscore >= minCreditScore)
            r.p.msgApplicationAccepted();
            r.state = RentPaid;
        else
            r.p.msgApplicationDenied();
            RenterList.remove(r);       	
    }


##Real Estate

    class House {
        int x, y location //for animating
        int value //for purchasing, may be used to calculate lease, maintenance
        Agent/Person occupant //do we need a list of occupants?
        int maintenanceThreshold
        int timeSinceLastMaintenance
    } 


##Agent: HouseRenter

###Data

    enum BillStates { Pending, Paid }
    Class Bill {
    	LandLord l
    	double amt
    	BillState status = Pending
    }
    House mHouse
    List<Bill> bills

###Actions

    PayBill(Bill b)
	    b.l.msgHereIsRent(this, b.amt)
	    b.state = BillStates.Paid

    CookInKitchen()        
        gotHungry = false 
        // this needs to be a bool or some state that is set somewhere
        //run timer for some period of time

    Maintain()    
        //run timer for some period of time, animate
    h.timeSinceLastMaintenance = 0;

###Messages

    msgHereIsBill(Landlord l, double total)
        bills.add(new Bill(l, total))

    msgLease(Landlord l, House h)
        mHouse = h;
        //some exchange of money

###Scheduler

    if (gotHungry) //assumes choice was made at some point to cook @ home
        CookInKitchen()

    If there exists a Bill b in bills such that b.status == Pending
        PayBill(Bill b)

    If there exists a Bill b in bills such that b.states == Paid
    	bills.remove(b)

    if h.timeSinceLastMaintenance > h.MaintenanceThreshold {
        Maintain()
    }
    
    else { 
        h.timeSinceLastMaintenance++ 
    }
