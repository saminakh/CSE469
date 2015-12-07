[radiant, dire] = process_data();

support = .012;
% ------------------------ COMBOS ----------------------

% find first candidate list
combo_data = [radiant; dire];
sum_data = sum(combo_data,1);
L1 = find(sum_data>=size(combo_data,1)*support);

% find second candidate list
C2 = nchoosek(L1, 2);
L2 = [];
F2 = [];
for x=1:size(C2, 1)
    item1 = C2(x,1);
    item2 = C2(x,2);
    counter = 0;
    % Count transactions with that pair
    for y=1:size(combo_data,1)
        if (combo_data(y,item1) ==1 && combo_data(y,item2) ==1)
            counter = counter + 1;
        end     
    end
    % check support
    if(counter >= size(combo_data,1)*support)
        % add to list of pairs that meet min support
        L2 = [L2;C2(x,:)];
    else
        % have fail list to ensure pruning
        F2 = [F2;C2(x,:)];
    end
end

save('combo_data.mat');