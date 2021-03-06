;; 59 juxt [m]

Takes set of funcs, returns new func that
takes variable num of args and returns seq containing
the result of applying each func left-to-right to list of args.

(= [21 6 1] ((__ + max min) 2 3 5 1 6 4))	
(= ["HELLO" 5] ((__ #(.toUpperCase %) count) "hello"))	
(= [2 6 4] ((__ :a :c :b) {:a 2, :b 4, :c 6, :d 8 :e 10}))

(fn [& x] (fn [& a] (map #(apply % a) x)))

(fn [& fs]
  (fn [& args]
    (for [f fs]
      (apply f args))))

user=> (source juxt)
(defn juxt 
  "Takes set of funcs,
  returns fn that is the juxtaposition of them.
  Returned fn takes a variable number of args,
  returns vec containing result of applying
  each fn to the args (left-to-right).
  ((juxt a b c) x) => [(a x) (b x) (c x)]"
  ([f] 
     (fn ([] [(f)])
         ([x] [(f x)])
         ([x y] [(f x y)])
         ([x y z] [(f x y z)])
         ([x y z & args] [(apply f x y z args)])))
  ([f g] 
     (fn
       ([] [(f) (g)])
       ([x] [(f x) (g x)])
       ([x y] [(f x y) (g x y)])
       ([x y z] [(f x y z) (g x y z)])
       ([x y z & args] [(apply f x y z args) (apply g x y z args)])))
  ([f g h] 
     (fn
       ([] [(f) (g) (h)])
       ([x] [(f x) (g x) (h x)])
       ([x y] [(f x y) (g x y) (h x y)])
       ([x y z] [(f x y z) (g x y z) (h x y z)])
       ([x y z & args] [(apply f x y z args) (apply g x y z args) (apply h x y z args)])))
  ([f g h & fs]
     (let [fs (list* f g h fs)]
       (fn
         ([] (reduce1 #(conj %1 (%2)) [] fs))
         ([x] (reduce1 #(conj %1 (%2 x)) [] fs))
         ([x y] (reduce1 #(conj %1 (%2 x y)) [] fs))
         ([x y z] (reduce1 #(conj %1 (%2 x y z)) [] fs))
         ([x y z & args] (reduce1 #(conj %1 (apply %2 x y z args)) [] fs))))))
