[radiant, dire] = process_data();

support = .02;

% -------------------- COUNTER PICKS -------------------


% find first candidate list
counter_data = [radiant, dire];
sum_data = sum(counter_data,1);
L1 = find(sum_data>=size(counter_data,1)*support);

% find second candidate list
C2 = nchoosek(L1, 2);
L2 = [];
F2 = [];
for x=1:size(C2, 1)
    item1 = C2(x,1);
    item2 = C2(x,2);
    counter = 0;
    % Count transactions with that pair
    for y=1:size(counter_data,1)
        if (counter_data(y,item1) ==1 && counter_data(y,item2) ==1)
            counter = counter + 1;
        end     
    end
    % check support
    if(counter >= size(counter_data,1)*support)
        % add to list of pairs that meet min support
        L2 = [L2;C2(x,:)];
    else
        % have fail list to ensure pruning
        F2 = [F2;C2(x,:)];
    end
end

for i = 1:size(L2,1)
   L2(i,2) = L2(i,2) - 112;
end

L2_new = [];
for i = 1:size(L2,1)
   if(L2(i,2) > 0 && L2(i,1) < 113)
       L2_new = [L2_new; L2(i,:)];
   end
end

save('counter_data.mat');