--partialSum ns : The list of partial sums
-- of the numeric list 'ns'
partialSum = \ns -> if null ns then 
                     []
                    else
                     head ns : 
				       zipWidth ( \n1 -> \n2 -> n1 + n2)  
                       ( partialSum ns) ( tail ns )


-- iterate f x : takes a function 'f' and an item
-- 'x' and returns the infinite list 
iterate = \f -> \x -> x : map f ( iterate f x)

-- reps : a list which has its nth item , a list 
-- composed of n copies of the number n 
reps = (1:[]) : map ( \ns -> head ns + 1:ns) reps

-- well not quite , requires a helper function to copy the 
-- head, n times, but in FULL Haskell ;
-- reps = [1]:map (\ns -> ( 1 + head ns ):( map ( + 1 ) ns ) ) reps



-- pascal : the infintie list which has, as its nth item 
-- a list formed by the nth row of pascals triangle

