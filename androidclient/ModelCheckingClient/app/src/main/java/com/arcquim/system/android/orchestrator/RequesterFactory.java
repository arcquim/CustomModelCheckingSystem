package com.arcquim.system.android.orchestrator;

import com.arcquim.system.android.transport.CheckProjectRequester;
import com.arcquim.system.android.transport.CreateProjectRequester;
import com.arcquim.system.android.transport.EditProjectRequester;
import com.arcquim.system.android.transport.EnterSystemRequester;
import com.arcquim.system.android.transport.GetProjectHistoryRequester;
import com.arcquim.system.android.transport.Requester;
import com.arcquim.system.android.transport.ResponseReceivedHandler;

import java.util.HashMap;
import java.util.Map;

public class RequesterFactory {

    private static final Map<Orchestrator.DesiredActionType, ? super Requester> map;

    static {
        map = new HashMap<>();
        map.put(Orchestrator.DesiredActionType.VIEW_PROJECTS, new EnterSystemRequester());
        map.put(Orchestrator.DesiredActionType.VIEW_PROJECT, new GetProjectHistoryRequester());
        map.put(Orchestrator.DesiredActionType.CREATE_PROJECT, new CreateProjectRequester());
        map.put(Orchestrator.DesiredActionType.EDIT_PROJECT, new EditProjectRequester());
        map.put(Orchestrator.DesiredActionType.CHECK_PROJECT, new CheckProjectRequester());
    }

    public Requester getRequester(Orchestrator.DesiredActionType desiredActionType, ResponseReceivedHandler handler) {
        if (!map.containsKey(desiredActionType)) {
            return null;
        }
        Requester requester = (Requester) map.get(desiredActionType);
        requester.setResponseReceivedHandler(handler);
        return requester;
    }

}
